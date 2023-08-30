
## Implementation Details.
- The app demonstrates a strong adherence to the MVVM Architecture, with its components thoughtfully organized into packages, ensuring modularity, scalability, and testability.
- Dagger Hilt
- Remote and Local data sources
- Repository
- Coroutines with Flow
- Retrofit for remote layer and Room DB for local layer
- Recyclerview adapter with DiffUtil
- Game Data classes for different layers with mappers
- Navigation Component/Graph

## App Specific:
- Repository will read database - if database is empty, it will fetch data from the API. Save it to the DB and pass data to view layer.
- Repository then processes GameEntities to convert them into Team and Team Details - to structure data for the UI layer.
- List and Details page
- Sorting on Wins, Draws, Losses, Win % and Total

## Time Spent
- 6-7 hours

## References
- Repeat on lifecycle -  https://medium.com/androiddevelopers/repeatonlifecycle-api-design-story-8670d1a7d333

## How to build/run app.
- Minimum API 24, Target SDK 33. Works in landscape and portrait mode.
