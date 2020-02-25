package com.example.tochkaapp.utils

import androidx.annotation.StringRes

/**
 * Created by Vladimir Kraev
 */

/**
 * Class to deliver error messages from ViewModel to View through LiveData
 */
class ErrorEvent(@StringRes errorMessage: Int) : Event<Int>(errorMessage)