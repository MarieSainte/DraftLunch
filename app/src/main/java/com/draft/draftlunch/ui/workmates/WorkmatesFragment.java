package com.draft.draftlunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.R;
import com.draft.draftlunch.ui.ViewModelFactory;

import java.util.List;

public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workmates, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.workmates_recyclerview);
        progressBar = view.findViewById(R.id.progressBar);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(WorkmatesViewModel.class);
        mViewModel.init();
        mViewModel.getUsers().observe(getViewLifecycleOwner(), this::updateView);




    }

    private void updateView(List<User> users) {
        if (users!=null && !users.isEmpty()){
            progressBar.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            WorkmatesAdapter workmatesAdapter = new WorkmatesAdapter(getContext(), (List<User>) users);
            recyclerView.setAdapter(workmatesAdapter);
            workmatesAdapter.notifyDataSetChanged();
        }else{
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}