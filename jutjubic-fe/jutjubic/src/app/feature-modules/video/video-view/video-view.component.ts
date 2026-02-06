import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VideoService } from '../service/video.service';
import { Video } from '../model/video.model';

@Component({
  selector: 'app-video-view',
  templateUrl: './video-view.component.html',
  styleUrls: ['./video-view.component.css']
})
export class VideoViewComponent implements OnInit {
  video!: Video;
  videoStreamUrl!: string;
  likes: number = 0;
  comments: any[] = [];
  newComment: string = '';

  constructor(
    private videoService: VideoService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    this.loadVideo(id);
  }

  loadVideo(id: number) {
    this.videoService.getById(id).subscribe(video => {
      this.video = video;

      // Video stream endpoint
      // this.videoStreamUrl = `http://localhost:8080/api/videos/${id}/stream`;
      this.videoService.getVideoFile(this.video.videoUrl).subscribe(blob => {
          this.videoStreamUrl = URL.createObjectURL(blob);
        });

      // Load likes
      this.videoService.getLikes(id).subscribe(l => this.likes = l);

      // Load comments
      this.videoService.getComments(id).subscribe(c => this.comments = c);
    });
  }

  postComment() {
    if (!this.newComment.trim()) return;

    const userId = 1; // demo user
    this.videoService.postComment(this.video.id, userId, this.newComment)
      .subscribe(comment => {
        this.comments.push(comment);
        this.newComment = '';
      });
  }
}
