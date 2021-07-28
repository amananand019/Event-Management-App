package com.devil.premises.unicorn

class Post {
    var desc: String = ""
    var image: String = ""
    var owner: String = ""
    var title: String = ""
    var date: String = ""

    constructor():this("","","","", ""){
    }

    constructor( desc: String,  image: String, owner: String, title: String, date:String){
        this.desc = desc
        this.image = image
        this.owner = owner
        this.title = title
        this.date = date
    }

}