package com.example.viewactivitypractice.datas

class ContactData() {
    var id : Int = 0
    var name : String = ""
    var phonenumber: String = ""

    constructor(id:Int, name:String, phonenumber: String) : this() {
        this.id = id
        this.name = name
        this.phonenumber = phonenumber
    }
}