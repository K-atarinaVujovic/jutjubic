import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularVideosComponent } from './popular-videos.component';

describe('PopularVideosComponent', () => {
  let component: PopularVideosComponent;
  let fixture: ComponentFixture<PopularVideosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PopularVideosComponent]
    });
    fixture = TestBed.createComponent(PopularVideosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
