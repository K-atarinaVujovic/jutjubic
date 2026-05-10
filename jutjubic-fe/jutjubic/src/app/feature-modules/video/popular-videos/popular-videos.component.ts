import { Component } from '@angular/core';
import { Video } from '../model/video.model';
import { VideoService } from '../service/video.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-popular-videos',
  templateUrl: './popular-videos.component.html',
  styleUrls: ['./popular-videos.component.css']
})
export class PopularVideosComponent {
  videos: Video[] = [];

  constructor(private videoService: VideoService, private router: Router) {}

  ngOnInit(): void {
    this.videoService.getPopular().subscribe(videos => {
      this.videos = videos;
    });
  }

  openVideo(video: Video) {
    this.router.navigate(['/video', video.id]);
  }
}
