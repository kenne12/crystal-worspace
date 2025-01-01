import {inject, Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {createPaginationOption, PageableData, Pagination} from '../models/page.model';
import {GeoPoint} from '../models/geo-point.model';

@Injectable({
  providedIn: 'root'
})
export class GeoPointService {

  private readonly API_URL: string = environment.apiUrl;
  private httpClient: HttpClient = inject(HttpClient);

  public getGeoPoints(pagination: Pagination): Observable<PageableData<GeoPoint>> {
    const params = createPaginationOption(pagination);

    return this.httpClient.get<PageableData<GeoPoint>>(`${ this.API_URL }/geo-points`, {params});
  }

  public deleteGeoPoint(id: string): Observable<any> {
    return this.httpClient.delete(`${ this.API_URL }/geo-points/${ id }`);
  }

  public createGeoPoint(geoPoint: { title: string, longitude: number, latitude: number }): Observable<any> {
    return this.httpClient.post(`${ this.API_URL }/geo-points`, geoPoint);
  }

  public computeDistance(geoPointIdA: number, geoPointIdB: number): Observable<number> {
    return this.httpClient.get<number>(`${ this.API_URL }/geo-points/compute-distance?geoPointIdA=${ geoPointIdA }&geoPointIdB=${ geoPointIdB }`);
  }
}
