# Community Module for Agridost App

This document describes the community module implementation that allows users to post questions, share problems, and interact with the agricultural community.

## Features Implemented

### 1. Community Activity (`CommunityActivity.java`)
- **Search functionality**: Users can search through community posts
- **Filter by crop type**: Filter posts by different crop types (Tomato, Pea, Cotton, Onion, etc.)
- **Post display**: Shows community posts in a scrollable list
- **Interactive elements**: Like, dislike, comment, and share functionality

### 2. Data Models
- **Post.java**: Represents community posts with author info, content, crop type, and engagement metrics
- **Comment.java**: Represents comments on posts with author info and content

### 3. User Interface Components
- **Post List**: RecyclerView displaying community posts with images, author info, and interaction buttons
- **Ask Community Dialog**: Full-screen dialog for users to post new questions
- **Comment Dialog**: Full-screen dialog for viewing and adding comments to posts
- **Filter Chips**: Horizontal scrollable chips for filtering posts by crop type

### 4. Navigation Integration
- Integrated with bottom navigation in `MainActivity`
- Community tab navigates to `CommunityActivity`
- Proper back navigation and activity lifecycle management

## UI Components

### Community Activity Layout (`activity_community.xml`)
- Search bar with notification bell and menu
- Filter section with crop type chips
- RecyclerView for posts
- Floating Action Button for "Ask Community"

### Post Item Layout (`item_community_post.xml`)
- Post image display
- Author information (name, location, time)
- Crop type indicator
- Post title and content
- Interaction buttons (like, dislike, comment, share)

### Ask Community Dialog (`dialog_ask_community.xml`)
- Title input field
- Content text area
- Crop type selection dropdown
- Photo upload placeholder
- Post/Cancel buttons

### Comment Dialog (`dialog_comments.xml`)
- Post title display
- Comments list
- Comment input field
- Post comment button

## Key Features

### 1. Post Management
- Create new posts with title, content, and crop type
- Display posts with author information and timestamps
- Support for post images (placeholder implemented)

### 2. Comment System
- View comments on posts
- Add new comments
- Like/unlike comments
- Real-time comment count updates

### 3. Interaction Features
- Like/dislike posts
- Share posts (WhatsApp integration ready)
- Translate functionality (UI ready)
- Search and filter capabilities

### 4. Crop Type Support
- Predefined crop types: Tomato, Pea, Cotton, Onion, Wheat, Rice
- Visual indicators for each crop type
- Filter functionality by crop type

## Integration with Main App

The community module is fully integrated with the main Agridost app:

1. **Bottom Navigation**: Community tab in the main navigation
2. **Activity Registration**: Properly registered in AndroidManifest.xml
3. **Navigation Flow**: Seamless navigation between main app and community
4. **Consistent UI**: Matches the app's design language and color scheme

## Future Enhancements

1. **Image Upload**: Implement actual image upload functionality
2. **Translation**: Add real translation service integration
3. **Push Notifications**: Implement notification system for new posts/comments
4. **User Authentication**: Integrate with user management system
5. **Data Persistence**: Add database storage for posts and comments
6. **Advanced Search**: Implement more sophisticated search and filtering
7. **Moderation**: Add content moderation features
8. **Analytics**: Track user engagement and popular posts

## Usage

1. Navigate to Community tab in the bottom navigation
2. Browse posts or use search/filter to find specific content
3. Tap "Ask Community" FAB to post a new question
4. Tap on any post to view and add comments
5. Use like/dislike buttons to engage with content
6. Share interesting posts with others

The community module provides a complete social platform for agricultural knowledge sharing and problem-solving within the Agridost ecosystem.
