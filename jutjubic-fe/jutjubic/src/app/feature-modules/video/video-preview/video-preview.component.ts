import { Component, Input } from '@angular/core';
import { Video } from '../model/video.model';
import { VideoService } from '../service/video.service';

@Component({
  selector: 'app-video-preview',
  templateUrl: './video-preview.component.html',
  styleUrls: ['./video-preview.component.css']
})
export class VideoPreviewComponent {
  @Input() video!: Video;
  thumbnailUrl!: string;

  
  constructor(private videoService: VideoService) {}

  ngOnInit(): void {
    if (this.video.thumbnailUrl) {
      this.videoService.getThumbnail(this.video.thumbnailUrl)
        .subscribe(blob => {
          this.thumbnailUrl = URL.createObjectURL(blob);
        });
    }
  }
}

