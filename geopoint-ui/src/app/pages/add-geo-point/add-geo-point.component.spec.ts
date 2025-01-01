import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGeoPointComponent } from './add-geo-point.component';

describe('AddGeoPointComponent', () => {
  let component: AddGeoPointComponent;
  let fixture: ComponentFixture<AddGeoPointComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddGeoPointComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddGeoPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
