# AI Creator Assistant Suite Migration Guide

This guide helps you migrate existing VYRA installations to include the new AI Creator Assistant Suite features.

## Overview

The AI Creator Assistant Suite adds comprehensive AI capabilities to VYRA, including:
- FanDNA™ behavioral profiling
- Multi-provider AI routing (OpenAI, Anthropic, Google AI)
- Real-time sentiment analysis
- Content optimization engine
- Predictive analytics
- Automated workflows

## Prerequisites

- Existing VYRA installation with basic schema
- Database backup (recommended)
- AI provider API keys (OpenAI, Anthropic, Google AI)

## Migration Steps

### Step 1: Backup Existing Data

```bash
# For SQLite (development)
cp database/vyra.db database/vyra.db.backup

# For PostgreSQL/Supabase (production)
# Use Supabase dashboard to create a backup
```

### Step 2: Apply AI Schema Updates

The AI tables have been added to the main `server/database/schema.sql` file. If you're updating an existing database:

#### For SQLite (Development)

```bash
# Run the migration script
node run-ai-migration.cjs
```

#### For PostgreSQL/Supabase (Production)

Execute the following SQL in your Supabase SQL Editor:

```sql
-- Fan behavior and preference profiles (FanDNA™)
CREATE TABLE IF NOT EXISTS fan_profiles (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    creator_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    engagement_score DECIMAL(5,2) DEFAULT 0.00 CHECK (engagement_score >= 0 AND engagement_score <= 100),
    monetization_potential DECIMAL(5,2) DEFAULT 0.00 CHECK (monetization_potential >= 0 AND monetization_potential <= 100),
    preferred_communication_time TEXT,
    content_preferences JSON,
    spending_tier VARCHAR(20) DEFAULT 'low' CHECK (spending_tier IN ('low', 'medium', 'high', 'whale')),
    total_spent DECIMAL(10,2) DEFAULT 0.00,
    avg_session_duration INTEGER DEFAULT 0,
    message_frequency DECIMAL(5,2) DEFAULT 0.00,
    response_rate DECIMAL(3,2) DEFAULT 0.00,
    last_interaction DATETIME,
    profile_data JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, creator_id)
);

-- AI model performance metrics
CREATE TABLE IF NOT EXISTS ai_model_metrics (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    provider_name VARCHAR(50) NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    request_type VARCHAR(50) NOT NULL CHECK (request_type IN ('response_generation', 'sentiment_analysis', 'content_optimization', 'transcription', 'image_analysis')),
    response_time_ms INTEGER NOT NULL,
    cost_per_request DECIMAL(10,6),
    accuracy_score DECIMAL(3,2),
    success_rate DECIMAL(3,2),
    error_rate DECIMAL(3,2),
    tokens_used INTEGER,
    creator_id TEXT REFERENCES users(id) ON DELETE SET NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Content optimization history
CREATE TABLE IF NOT EXISTS content_optimization_history (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    creator_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    original_content TEXT NOT NULL,
    optimized_content TEXT NOT NULL,
    optimization_type VARCHAR(50) NOT NULL CHECK (optimization_type IN ('engagement', 'monetization', 'sentiment', 'hashtags', 'timing')),
    suggestions JSON,
    performance_metrics JSON,
    was_used BOOLEAN DEFAULT FALSE,
    effectiveness_score DECIMAL(3,2),
    ai_provider VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Sentiment analysis results
CREATE TABLE IF NOT EXISTS sentiment_analysis_results (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    message_id TEXT REFERENCES messages(id) ON DELETE CASCADE,
    conversation_id TEXT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    sentiment_score DECIMAL(3,2) NOT NULL CHECK (sentiment_score >= -1.00 AND sentiment_score <= 1.00),
    sentiment_label VARCHAR(20) NOT NULL CHECK (sentiment_label IN ('very_negative', 'negative', 'neutral', 'positive', 'very_positive', 'concerning')),
    emotion_tags JSON,
    confidence_score DECIMAL(3,2) CHECK (confidence_score >= 0.00 AND confidence_score <= 1.00),
    analysis_provider VARCHAR(50),
    requires_attention BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Predictive analytics data
CREATE TABLE IF NOT EXISTS predictive_analytics (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    creator_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    prediction_type VARCHAR(50) NOT NULL CHECK (prediction_type IN ('revenue_forecast', 'fan_churn', 'content_performance', 'optimal_pricing', 'growth_opportunity')),
    prediction_data JSON NOT NULL,
    confidence_interval JSON,
    actual_outcome JSON,
    accuracy_score DECIMAL(3,2),
    model_version VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME
);

-- AI workflow automation logs
CREATE TABLE IF NOT EXISTS ai_workflow_logs (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    creator_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    workflow_type VARCHAR(50) NOT NULL CHECK (workflow_type IN ('fan_onboarding', 'content_scheduling', 'response_automation', 'moderation', 'upselling')),
    trigger_event VARCHAR(100) NOT NULL,
    actions_taken JSON NOT NULL,
    success BOOLEAN DEFAULT TRUE,
    error_message TEXT,
    execution_time_ms INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### Step 3: Create Indexes

```sql
-- Fan profiles indexes
CREATE INDEX IF NOT EXISTS idx_fan_profiles_user_creator ON fan_profiles(user_id, creator_id);
CREATE INDEX IF NOT EXISTS idx_fan_profiles_creator_engagement ON fan_profiles(creator_id, engagement_score DESC);
CREATE INDEX IF NOT EXISTS idx_fan_profiles_monetization ON fan_profiles(creator_id, monetization_potential DESC);
CREATE INDEX IF NOT EXISTS idx_fan_profiles_updated ON fan_profiles(updated_at DESC);

-- AI metrics indexes
CREATE INDEX IF NOT EXISTS idx_ai_metrics_provider_model ON ai_model_metrics(provider_name, model_name);
CREATE INDEX IF NOT EXISTS idx_ai_metrics_timestamp ON ai_model_metrics(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_ai_metrics_creator ON ai_model_metrics(creator_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_ai_metrics_performance ON ai_model_metrics(request_type, response_time_ms);

-- Content optimization indexes
CREATE INDEX IF NOT EXISTS idx_content_opt_creator ON content_optimization_history(creator_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_content_opt_type ON content_optimization_history(optimization_type, was_used);
CREATE INDEX IF NOT EXISTS idx_content_opt_effectiveness ON content_optimization_history(effectiveness_score DESC);

-- Sentiment analysis indexes
CREATE INDEX IF NOT EXISTS idx_sentiment_message ON sentiment_analysis_results(message_id);
CREATE INDEX IF NOT EXISTS idx_sentiment_conversation ON sentiment_analysis_results(conversation_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_sentiment_user ON sentiment_analysis_results(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_sentiment_attention ON sentiment_analysis_results(requires_attention) WHERE requires_attention = TRUE;
CREATE INDEX IF NOT EXISTS idx_sentiment_score ON sentiment_analysis_results(sentiment_score, created_at DESC);

-- Predictive analytics indexes
CREATE INDEX IF NOT EXISTS idx_predictions_creator_type ON predictive_analytics(creator_id, prediction_type);
CREATE INDEX IF NOT EXISTS idx_predictions_expires ON predictive_analytics(expires_at);
CREATE INDEX IF NOT EXISTS idx_predictions_accuracy ON predictive_analytics(accuracy_score DESC);

-- Workflow logs indexes
CREATE INDEX IF NOT EXISTS idx_workflow_creator_type ON ai_workflow_logs(creator_id, workflow_type);
CREATE INDEX IF NOT EXISTS idx_workflow_timestamp ON ai_workflow_logs(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_workflow_success ON ai_workflow_logs(success, created_at DESC);
```

### Step 4: Create Triggers

```sql
-- Update fan profiles timestamp
CREATE TRIGGER IF NOT EXISTS update_fan_profiles_updated_at 
    AFTER UPDATE ON fan_profiles
    BEGIN
        UPDATE fan_profiles SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id;
    END;

-- Auto-update fan profile when new message is sent
CREATE TRIGGER IF NOT EXISTS update_fan_profile_on_message
    AFTER INSERT ON messages
    BEGIN
        INSERT OR REPLACE INTO fan_profiles (
            user_id, creator_id, last_interaction, updated_at
        ) VALUES (
            CASE 
                WHEN (SELECT role FROM users WHERE id = NEW.sender_id) = 'fan' 
                THEN NEW.sender_id 
                ELSE (SELECT fan_id FROM conversations WHERE id = NEW.conversation_id)
            END,
            CASE 
                WHEN (SELECT role FROM users WHERE id = NEW.sender_id) = 'creator' 
                THEN NEW.sender_id 
                ELSE (SELECT creator_id FROM conversations WHERE id = NEW.conversation_id)
            END,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        );
    END;
```

### Step 5: Configure Environment Variables

Add the following environment variables to your `.env` file:

```env
# AI Provider API Keys
OPENAI_API_KEY=your_openai_api_key
ANTHROPIC_API_KEY=your_anthropic_api_key
GOOGLE_AI_API_KEY=your_google_ai_api_key

# AI Configuration (Optional)
AI_DEFAULT_PROVIDER=openai
AI_DAILY_BUDGET_LIMIT=50.00
AI_MONTHLY_BUDGET_LIMIT=1000.00
```

### Step 6: Install AI Dependencies

```bash
# Install required AI service dependencies
pnpm install
```

### Step 7: Test AI Features

```bash
# Run tests to verify AI functionality
pnpm test server/services/ai/

# Test database schema
pnpm typecheck
```

## Verification Steps

### 1. Verify Database Schema

Check that all AI tables exist:

```sql
-- List all tables (should include AI tables)
SELECT name FROM sqlite_master WHERE type='table';

-- Or for PostgreSQL:
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';
```

### 2. Test FanDNA™ Profile Creation

Send a message between a creator and fan, then check:

```sql
SELECT * FROM fan_profiles WHERE user_id = 'fan_user_id' AND creator_id = 'creator_user_id';
```

### 3. Verify AI Service Integration

```typescript
// Test AI service initialization
import { AIServiceManager } from './server/services/ai/ai-service-manager.js';

const aiService = new AIServiceManager(config);
const health = aiService.getProviderHealth();
console.log('AI Providers Health:', health);
```

## Rollback Plan

If you need to rollback the migration:

### For SQLite

```bash
# Restore from backup
cp database/vyra.db.backup database/vyra.db
```

### For PostgreSQL/Supabase

```sql
-- Drop AI tables (in reverse order due to dependencies)
DROP TABLE IF EXISTS ai_workflow_logs;
DROP TABLE IF EXISTS predictive_analytics;
DROP TABLE IF EXISTS sentiment_analysis_results;
DROP TABLE IF EXISTS content_optimization_history;
DROP TABLE IF EXISTS ai_model_metrics;
DROP TABLE IF EXISTS fan_profiles;

-- Drop triggers
DROP TRIGGER IF EXISTS update_fan_profile_on_message;
DROP TRIGGER IF EXISTS update_fan_profiles_updated_at;
```

## Post-Migration Tasks

### 1. Update Application Code

Ensure your application code is updated to use the new AI features:

- Import AI services in your WebSocket handlers
- Add sentiment analysis to message processing
- Integrate FanDNA™ profile updates

### 2. Configure AI Monitoring

Set up monitoring for:
- AI provider costs and usage
- Response times and accuracy
- Error rates and failures

### 3. Train AI Models

If using custom models:
- Collect initial training data
- Fine-tune models for your specific use case
- Set up continuous learning pipelines

## Troubleshooting

### Common Issues

1. **Foreign Key Constraints**: Ensure existing users and conversations exist before creating AI records
2. **API Key Errors**: Verify all AI provider API keys are valid and have sufficient credits
3. **Performance Issues**: Monitor database query performance with new indexes
4. **Memory Usage**: AI operations may increase memory usage, monitor server resources

### Getting Help

- Check the AI service logs for detailed error information
- Review the AI Creator Assistant Suite documentation
- Test individual AI providers to isolate issues
- Monitor database performance after migration

## Next Steps

After successful migration:

1. Configure AI provider preferences and budgets
2. Set up automated workflows for fan onboarding
3. Enable real-time sentiment analysis in chat
4. Configure content optimization suggestions
5. Set up predictive analytics dashboards

The AI Creator Assistant Suite is now ready to enhance your VYRA platform with intelligent automation and insights!