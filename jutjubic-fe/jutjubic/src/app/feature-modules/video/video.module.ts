import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoPreviewComponent } from './video-preview/video-preview.component';
import { VideoViewComponent } from './video-view/video-view.component';
import { FormsModule } from '@angular/forms';
import { A11yModule } from "@angular/cdk/a11y";



@NgModule({
  declarations: [
    VideoPreviewComponent,
    VideoViewComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    A11yModule
],
  exports: [
    VideoPreviewComponent
  ]
})
export class VideoModule { }
