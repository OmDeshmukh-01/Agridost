# OpenAI API Integration Setup

This document explains how to set up the OpenAI API integration for the AgriDost chatbot.

## Prerequisites

1. An OpenAI account with API access
2. An OpenAI API key

## Getting Your API Key

1. Go to [OpenAI Platform](https://platform.openai.com/)
2. Sign in to your account or create a new one
3. Navigate to [API Keys](https://platform.openai.com/api-keys)
4. Click "Create new secret key"
5. Give it a name (e.g., "AgriDost App")
6. Copy the generated API key (it starts with `sk-`)

## Setting Up the API Key

1. Open the file: `app/src/main/java/com/example/dummy/ApiConfig.java`
2. Find the line: `public static final String OPENAI_API_KEY = "YOUR_API_KEY_HERE";`
3. Replace `"YOUR_API_KEY_HERE"` with your actual API key
4. Save the file

Example:
```java
public static final String OPENAI_API_KEY = "sk-your-actual-api-key-here";
```

## API Configuration

The following settings can be modified in `ApiConfig.java`:

- **Model**: Currently set to `gpt-3.5-turbo` (cost-effective and fast)
- **Max Tokens**: Set to 500 (adjust based on your needs)
- **Temperature**: Set to 0.7 (balance between creativity and consistency)
- **System Prompt**: Customized for agricultural assistance

## Testing the Integration

1. Build and run the app
2. Navigate to the chat interface
3. Send a test message like "How do I identify plant diseases?"
4. You should receive a response from the OpenAI API

## Troubleshooting

### Common Issues

1. **"Please set your OpenAI API key" message**
   - Make sure you've replaced the placeholder in `ApiConfig.java`
   - Ensure the API key is correctly formatted (starts with `sk-`)

2. **Network errors**
   - Check your internet connection
   - Verify the API key is valid and has sufficient credits

3. **API rate limits**
   - OpenAI has rate limits based on your account tier
   - Consider upgrading your plan if you hit limits frequently

### Error Messages

- **401 Unauthorized**: Invalid API key
- **429 Too Many Requests**: Rate limit exceeded
- **500 Internal Server Error**: OpenAI service issue

## Security Notes

- Never commit your API key to version control
- Consider using environment variables or secure storage for production apps
- Monitor your API usage to avoid unexpected charges

## Cost Considerations

- GPT-3.5-turbo is cost-effective for most use cases
- Monitor your usage in the OpenAI dashboard
- Set up billing alerts to avoid unexpected charges

## Support

For issues with the OpenAI API, visit the [OpenAI Help Center](https://help.openai.com/).
For app-specific issues, check the Android logs for detailed error messages.
