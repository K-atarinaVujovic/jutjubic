import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleVideoPromptComponent } from './schedule-video-prompt.component';

describe('ScheduleVideoPromptComponent', () => {
  let component: ScheduleVideoPromptComponent;
  let fixture: ComponentFixture<ScheduleVideoPromptComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScheduleVideoPromptComponent]
    });
    fixture = TestBed.createComponent(ScheduleVideoPromptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
