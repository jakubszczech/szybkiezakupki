package com.example.szybkiezakupki.utils

data class UserData (var name:String, var surname_address:String, var acctype:Boolean? =null)
{
      constructor(): this ("","")
  }