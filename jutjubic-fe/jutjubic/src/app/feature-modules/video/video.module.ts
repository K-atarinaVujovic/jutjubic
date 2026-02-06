import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoPreviewComponent } from './video-preview/video-preview.component';
import { VideoViewComponent } from './video-view/video-view.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    VideoPreviewComponent,
    VideoViewComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
  ],
  exports: [
    VideoPreviewComponent
  ]
})
export class VideoModule { }
