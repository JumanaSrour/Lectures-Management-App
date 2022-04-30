package com.example.schoolie.models

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}
