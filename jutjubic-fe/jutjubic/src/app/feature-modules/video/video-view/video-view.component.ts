import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VideoService } from '../service/video.service';
import { Video } from '../model/video.model';
import { Comment } from '../model/comment.model';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import Hls from 'hls.js';

@Component({
  selector: 'app-video-view',
  templateUrl: './video-view.component.html',
  styleUrls: ['./video-view.component.css']
})
export class VideoViewComponent implements OnInit {
  @ViewChild('videoPlayer') videoPlayer!: ElementRef<HTMLVideoElement>;

  video!: Video;
  videoStreamUrl!: string;
  likes: number = 0;
  comments: Comment[] = [];
  newComment: string = '';

  constructor(
    private videoService: VideoService,
    private route: ActivatedRoute,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    this.loadVideo(id);
    // this.loadVideoHls(id);
  }

  // ngAfterViewInit(): void {
  //   const id = +this.route.snapshot.params['id'];
  //   this.loadVideoHls(id);
  // }

  loadVideo(id: number) {
    this.videoService.getById(id).subscribe(video => {
      this.video = video;
      console.log(video);

      // Video stream endpoint
      // this.videoStreamUrl = `http://localhost:8080/api/videos/${id}/stream`;
      // this.videoService.getVideoFile(this.video.id).subscribe(blob => {
      //     this.videoStreamUrl = URL.createObjectURL(blob);
      //   });

      // Load likes
      this.videoService.getLikes(id).subscribe(l => this.likes = l);

      // Load comments
      this.videoService.getComments(id).subscribe(c => this.comments = c);

      setTimeout(() => this.loadVideoHls(id), 0);
    });
  }

  loadVideoHls(id: number){
    const video = this.videoPlayer?.nativeElement;
    const manifestUrl = `http://localhost:8080/api/videos/${id}/hls/index.m3u8`;
  
    if(Hls.isSupported()){
      const hls = new Hls();
      hls.loadSource(manifestUrl);
      hls.attachMedia(video);
    }
    else{
      video.src = manifestUrl;
    }

    // prevent forwarding if live
    video.addEventListener('seeking', () => {
      if (this.video?.live) {
        const expectedTime = this.calculateOffset();
        if (video.currentTime > expectedTime) {
          video.currentTime = expectedTime;
        }
      }
    });
  }

  private calculateOffset(): number {
    const scheduledAt = new Date(this.video.scheduledAt);
    return (new Date().getTime() - scheduledAt.getTime()) / 1000;
  }

  onMetadataLoaded(): void {
    if (this.video.live) {
      const offsetSeconds = this.calculateOffset();
      this.videoPlayer.nativeElement.currentTime = offsetSeconds;
    }
  }

  postComment() {
    if (!this.newComment.trim()) return;

    const userId = this.authService.currentUser.id;
    this.videoService.postComment(this.video.id, userId, this.newComment)
      .subscribe(comment => {
        this.comments.push(comment);
        this.newComment = '';
      });
  }

  likeVideo(){
    const userId = this.authService.currentUser.id;
    const videoId = this.video.id;
    this.videoService.hasUserLiked(videoId, userId).subscribe({
      next: (result) => {
        if(result == true){
          this.unlikeVideo();
        }
        else{
          this.videoService.like(videoId, userId).subscribe({
            next: (result) => {
              this.likes += 1;
            },
            error: (err) => {
              
            }
          });
        }
      },
      error: (err) => {
        
      }
    })
    
  }
  
  unlikeVideo(){
    const userId = this.authService.currentUser.id;
    const videoId = this.video.id;
    this.videoService.removeLike(videoId, userId).subscribe({
      next: (result) => {
        this.likes -= 1;
      },
      error: (err) => {
        
      }
    });
  }
}
