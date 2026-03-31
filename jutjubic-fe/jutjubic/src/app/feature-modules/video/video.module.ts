import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoPreviewComponent } from './video-preview/video-preview.component';
import { VideoViewComponent } from './video-view/video-view.component';
import { FormsModule } from '@angular/forms';
import { A11yModule } from "@angular/cdk/a11y";
import { VideoUploadComponent } from './video-upload/video-upload.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ScheduleVideoPromptComponent } from './video-upload/schedule-video-prompt/schedule-video-prompt.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [
    VideoPreviewComponent,
    VideoViewComponent,
    VideoUploadComponent,
    ScheduleVideoPromptComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    A11yModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatFormFieldModule,
],
  exports: [
    VideoPreviewComponent
  ]
})
export class VideoModule { }
