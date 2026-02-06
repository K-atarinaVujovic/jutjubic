import { Component, OnInit } from '@angular/core';
import { VideoService } from '../../video/service/video.service';
import { Video } from '../../video/model/video.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  videos: Video[] = [];

  constructor(private videoService: VideoService, private router: Router) {}

  ngOnInit(): void {
    this.videoService.getAllSorted().subscribe(videos => {
      this.videos = videos;
    });
  }

  openVideo(video: Video) {
    this.router.navigate(['/video', video.id]);
  }
}
