package com.example.mystoryapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.data.api.ApiConfigStory
import com.example.mystoryapp.data.api.ApiConfigUser
import com.example.mystoryapp.data.api.RemoteDataSourceStory
import com.example.mystoryapp.data.api.RemoteDataSourceUser
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.databinding.FragmentMainBinding
import com.example.mystoryapp.repositories.StoryRepository
import com.example.mystoryapp.repositories.UserRepository
import com.example.mystoryapp.utils.Result
import com.example.mystoryapp.viewModels.StoryViewModel
import com.example.mystoryapp.viewModels.StoryViewModelFactory
import com.example.mystoryapp.viewModels.UserViewModel
import com.example.mystoryapp.viewModels.UserViewModelFactory

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAdapter: StoryAdapter

    private val userViewModel: UserViewModel by activityViewModels{
        UserViewModelFactory(UserRepository.getInstance(RemoteDataSourceUser(ApiConfigUser), UserPreference(requireContext())))
    }

    private val storyViewModel: StoryViewModel by activityViewModels{
        StoryViewModelFactory(StoryRepository.getInstance(RemoteDataSourceStory(UserPreference(requireContext()))))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container,false)
        val view = binding.root

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        setupRecyclerView()
        observerStories()
    }

    private fun observerStories(){
        storyViewModel.storyResponseData.observe(viewLifecycleOwner, Observer{result ->
            when(result){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    result.data?.let {
                        Log.d("MainFragment", "Data received: ${it.size} stories")
                        it.forEach { story ->
                            Log.d("MainFragment", "Story: ${story.name}")
                        }
                        storyAdapter.submitList(it.toMutableList())
                        Log.d("MainFragment", "Data received: ${it.size} stories")
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupRecyclerView(){
        storyAdapter = StoryAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = storyAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_logout -> {
                userViewModel.logoutUser()
                findNavController().navigate(R.id.action_mainFragment_to_onBoardingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}