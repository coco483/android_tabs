package com.example.viewactivitypractice.datas

class ContactData() {
    var id : Int = 0
    var name : String = ""
    var phonenumber: String = ""

    constructor(name:String, phonenumber: String) : this() {
        this.name = name
        this.phonenumber = phonenumber
    }
}