package com.pashacabu.rentateamtestapp.data

sealed class State(_message : String = ""){

    val message = _message

    object Loading : State()
    object Loaded : State("Loaded!")
    object Error : State("Error occurred!")
    object ListEnd : State("End of list!")
    object NoConnection : State("No connection")
}


