import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListGeoPointComponent } from './list-geo-point.component';

describe('ListGeoPointComponent', () => {
  let component: ListGeoPointComponent;
  let fixture: ComponentFixture<ListGeoPointComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListGeoPointComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListGeoPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
