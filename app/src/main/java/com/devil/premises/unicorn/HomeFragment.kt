package com.devil.premises.unicorn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

private const val TAG = "HomeFragment"
class HomeFragment: Fragment(R.layout.fragment_home) {

    lateinit var ref: DatabaseReference
    private lateinit var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Post, RViewHolder>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().reference.child("posts")
        val userRef = FirebaseDatabase.getInstance().reference.child("users")
        val ll = LinearLayoutManager(requireContext())
//        ll.stackFromEnd = true
        ll.reverseLayout = true
        homeRecyclerView.layoutManager = ll

//        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
//        val todayDate = Calendar.getInstance().toString()
//        val format = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
//        val cal = Calendar.getInstance(Locale.ENGLISH)
//        val date = DateFormat.format("dd-MM-yyyy", cal.timeInMillis).toString()

        val option = FirebaseRecyclerOptions.Builder<Post>().setQuery(ref, Post::class.java).build()
        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Post, RViewHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder {
                val itemView = LayoutInflater.from(context).inflate(R.layout.home_post_grid_item, parent, false)
                return RViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: RViewHolder, position: Int, model: Post) {
                ref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //val eventDate = model.date
//                        val date = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(eventDate)
                        //problem is here
                        //if(format.parse(todayDate).before(format.parse(eventDate))){
                            holder.rTitle.text = model.title
                            holder.rDate.text = model.date

                            userRef.child(model.owner).get().addOnSuccessListener {
                                holder.rOwner.text = it.child("name").value.toString()
                            }

                            if(model.image == "No image"){
                                holder.rImage.visibility = View.GONE
                            }else{
                                Glide.with(holder.itemView.context).load(model.image).into(holder.rImage)
                            }

                            holder.itemView.setOnClickListener {
                                val bundle = Bundle()
                                bundle.putString("title", model.title)
                                bundle.putString("date", model.date)
                                bundle.putString("desc", model.desc)
                                bundle.putString("image", model.image)
                                bundle.putString("owner", holder.rOwner.text.toString())

                                val intent = Intent(context, PostDescriptionActivity::class.java)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        //}
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled: ${error.message}")
                    }
                })
            }
        }


        homeRecyclerView.adapter = firebaseRecyclerAdapter
    }

    class RViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var rImage: ImageView = itemView.findViewById(R.id.postImage)
        var rOwner:TextView = itemView.findViewById(R.id.postOwner)
        var rTitle: TextView = itemView.findViewById(R.id.postTitle)
        var rDate: TextView = itemView.findViewById(R.id.postDate)


    }

    override fun onStart() {
        super.onStart()
        firebaseRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        firebaseRecyclerAdapter.stopListening()
    }
}