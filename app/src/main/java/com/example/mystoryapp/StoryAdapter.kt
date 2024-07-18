package com.example.mystoryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.api.responses.Story

class StoryAdapter: ListAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item_layout, parent, false)
        return StoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        Log.d("StoryAdapter", "Binding story at position $position: ${getItem(position)}")
        print("Binding story at position $position: ${getItem(position)}")
    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val storyImageView: ImageView = itemView.findViewById(R.id.storyImageView)
        private val nameStoryTextView: TextView = itemView.findViewById(R.id.nameStoryTextView)
        private val descStoryTextView: TextView = itemView.findViewById(R.id.descTextView)

        fun bind(story: Story){
            Glide.with(itemView.context).load(story.photoUrl).into(storyImageView)
            nameStoryTextView.text = story.name
            descStoryTextView.text = story.description

            itemView.setOnClickListener{

            }
        }
    }
    
}