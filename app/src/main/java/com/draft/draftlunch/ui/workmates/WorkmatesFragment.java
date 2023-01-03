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


        mViewModel.getUsers().observe(getViewLifecycleOwner(), this::updateView);

        recyclerView = view.findViewById(R.id.workmates_recyclerview);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void updateView(List<User> users) {
        if (users!=null && !users.isEmpty()){
            progressBar.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            WorkmatesAdapter workmatesAdapter = new WorkmatesAdapter(getContext(), (List<User>) mViewModel.getUsers());
            recyclerView.setAdapter(workmatesAdapter);
            workmatesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);

        mViewModel.init();
    }


}