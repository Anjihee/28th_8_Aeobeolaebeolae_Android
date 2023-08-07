package com.surround2023.surround2023.user_login_join

class UserSingleton private constructor() {

    private var userData: User? = null

    fun setUserData(user: User) {
        userData = user
    }

    fun getUserData(): User? {
        return userData
    }

    companion object {
        @Volatile
        private var instance: UserSingleton? = null

        fun getInstance(): UserSingleton =
            instance ?: synchronized(this) {
                instance ?: UserSingleton().also { instance = it }
            }
    }
}