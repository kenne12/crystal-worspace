import {Routes} from '@angular/router';
import {ListGeoPointComponent} from './pages/list-geo-point/list-geo-point.component';
import {AddGeoPointComponent} from './pages/add-geo-point/add-geo-point.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'geo-points',
    pathMatch: 'full'
  },
  {
    path: 'geo-points',
    component: ListGeoPointComponent
  },
  {
    path: 'add-geo-point',
    component: AddGeoPointComponent
  }
];
