package com.devil.premises.unicorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_post_description.*

class PostDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_description)

        val bundle = intent.extras
        val title = bundle!!.getString("title", "default")
        val date = bundle.getString("date", "default")
        val desc = bundle.getString("desc", "default")
        val image = bundle.getString("image", "default")
        val owner = bundle.getString("owner", "default")

        postTitle.text = title
        postDate.text = date
        postDesc.text = desc
        postOwner.text = owner

        Glide.with(applicationContext).load(image).into(postImage)
    }
}