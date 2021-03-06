/*
 * Copyright 2018 Antoine PURNELLE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ouftech.popularmovies.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ouftech.popularmovies.R;
import net.ouftech.popularmovies.commons.CollectionUtils;
import net.ouftech.popularmovies.commons.Logger;
import net.ouftech.popularmovies.model.Video;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    @NonNull
    protected String getLotTag() {
        return "VideosAdapter";
    }

    private List<Video> videos;

    public VideosAdapter() {
    }

    public void swapData(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        int videosCount = CollectionUtils.getSize(videos);
        if (videos == null || videosCount == 0 || position < 0 || position >= videosCount) {
            Logger.e(getLotTag(), new ArrayIndexOutOfBoundsException(String.format("Cannot bind item at position %s. Videos count is %s", position, videosCount)));
            return;
        }

        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.getSize(videos);
    }

}
