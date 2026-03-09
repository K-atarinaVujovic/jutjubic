import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoPreviewComponent } from './video-preview/video-preview.component';
import { VideoViewComponent } from './video-view/video-view.component';
import { FormsModule } from '@angular/forms';
import { A11yModule } from "@angular/cdk/a11y";
import { VideoUploadComponent } from './video-upload/video-upload.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    VideoPreviewComponent,
    VideoViewComponent,
    VideoUploadComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    A11yModule,
    ReactiveFormsModule,
],
  exports: [
    VideoPreviewComponent
  ]
})
export class VideoModule { }
