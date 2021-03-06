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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.ouftech.popularmovies.R;
import net.ouftech.popularmovies.commons.NetworkUtils;
import net.ouftech.popularmovies.model.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.video_card_thumbnail_iv)
    protected ImageView thumbnailIV;
    @BindView(R.id.video_card_title_tv)
    protected TextView titleTV;

    public VideoViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull final Video video) {
        titleTV.setText(video.name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.openYoutubeVideo(itemView.getContext(), video.key);
            }
        });


        Glide.with(itemView.getContext())
                .load(NetworkUtils.getYoutubeThumbnailURL(video.key))
                .into(thumbnailIV);
    }


}
