//package com.example.serona.data.repository
//
//import android.util.Log
//import com.example.serona.data.api.FavoriteRequest
//import com.example.serona.data.api.TutorialApi
//import com.example.serona.data.model.Tutorial
//import javax.inject.Inject
//
//class TutorialRepository @Inject constructor(
//    private val api: TutorialApi
//) {
//    // Get all tutorials
//    suspend fun getTutorials(): List<Tutorial> {
//        return try {
//            Log.d("TutorialRepo", "🔄 Calling API...")
//
//            val response = api.getTutorialsRaw()   // ← kita buat endpoint RAW dulu
//            Log.d("TutorialRepo", "RAW RESPONSE: $response")
//
//            val result = api.getTutorials()
//            result
//        } catch (e: Exception) {
//            Log.e("TutorialRepo", "❌ API Error: ${e.message}")
//            getDummyTutorials()
//        }
//    }
//
//
//    // Get tutorial by ID
//    suspend fun getTutorialById(id: Int): Tutorial {
//        return api.getTutorial(id)
//    }
//
//    // Get user's favorites
//    suspend fun getFavorites(): List<Tutorial> {
//        return try {
//            api.getFavorites()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList()
//        }
//    }
//
//    // Add to favorites
//    suspend fun addFavorite(tutorialId: Int): Boolean {
//        return try {
//            val response = api.addFavorite(FavoriteRequest(tutorialId))
//            response.isSuccessful
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }
//
//    // Remove from favorites
//    suspend fun removeFavorite(tutorialId: Int): Boolean {
//        return try {
//            val response = api.removeFavorite(tutorialId)
//            response.isSuccessful
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }
//
//    // TEMPORARY: Dummy data for testing UI
//    private fun getDummyTutorials(): List<Tutorial> {
//        return listOf(
//            Tutorial(
//                id = 1,
//                title = "Makeup for Oval Face",
//                description = "Tutorial makeup untuk wajah oval",
//                image_url = "https://via.placeholder.com/300x200",
//                mainCategory = "Face Shape",
//                subCategory = "Oval",
//                isFavorite = false
//            ),
//            Tutorial(
//                id = 2,
//                title = "Party Makeup Look",
//                description = "Tutorial makeup untuk pesta",
//                image_url = "https://via.placeholder.com/300x200",
//                mainCategory = "Occasion",
//                subCategory = "Party",
//                isFavorite = false
//            ),
//            Tutorial(
//                id = 3,
//                title = "Makeup for Fair Skin",
//                description = "Tutorial makeup untuk kulit cerah",
//                image_url = "https://via.placeholder.com/300x200",
//                mainCategory = "Skin Tone",
//                subCategory = "Fair-Light",
//                isFavorite = false
//            )
//        )
//    }
//}
package com.serona.app.data.repository

import android.util.Log
import com.serona.app.data.api.TutorialApi
import com.serona.app.data.model.Tutorial
import com.serona.app.data.api.FavoriteRequest
import javax.inject.Inject
class TutorialRepository @Inject constructor(
    private val api: TutorialApi
) {
    // Get all tutorials
//    suspend fun getTutorials(): List<Tutorial> {
//        return try {
//            api.getTutorials()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList()
//        }
//    }

    // Di TutorialViewModel, coba bypass auth dulu untuk testing
    suspend fun getTutorials(): List<Tutorial> {
        return try {
            val response = api.getTutorials()

            Log.d("TutorialVM", """
            SUCCESS: ${response.success}
            MESSAGE: ${response.message}
            TOTAL: ${response.data.size}
        """.trimIndent())

            response.data       // ← INI YANG BENER
        } catch (e: Exception) {
            Log.e("TutorialVM", "Error: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    // Get tutorial by ID
    suspend fun getTutorialById(id: Int): Tutorial {
        val response = api.getTutorial(id)
        return response.data
    }

//    suspend fun getSteps(id: Int): List<TutorialStep> {
//        return try {
//            api.getTutorialSteps(id)
//        } catch (e: Exception) {
//            Log.e("STEPS", e.message.toString())
//            emptyList()
//        }
//    }

    // Get user's favorites
    suspend fun getFavorites(): List<Tutorial> {
        val response = api.getFavorites()

        return response.data.map { favItem ->
            favItem.tutorial
        }
    }

    // Add to favorites
    suspend fun addFavorite(tutorialId: Int): Boolean {
        return try {
            val response = api.addFavorite(FavoriteRequest(tutorialId))
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Remove from favorites
    suspend fun removeFavorite(tutorialId: Int): Boolean {
        return try {
            val response = api.removeFavorite(tutorialId)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
