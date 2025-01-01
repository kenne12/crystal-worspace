import {Component, computed, inject, model, OnInit, signal} from '@angular/core';
import {GeoPointService} from '../../services/geo-point.service';
import {PageableData, Pagination} from '../../models/page.model';
import {GeoPoint} from '../../models/geo-point.model';
import {FormsModule} from '@angular/forms';
import {DialogService} from '../../services/dialog.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-list-geo-point',
  imports: [
    FormsModule
  ],
  templateUrl: './list-geo-point.component.html',
  styleUrl: './list-geo-point.component.scss'
})
export class ListGeoPointComponent implements OnInit {

  private positionService = inject(GeoPointService);
  private dialogService = inject(DialogService);

  private matSnackBar = inject(MatSnackBar);

  private pageRequest = signal<Pagination>({
    page: 0,
    size: 2,
  });

  public distance = signal<number>(0);

  public pointIdA = model<number>();
  public pointIdB = model<number>();
  public geoPointPage = signal<PageableData<GeoPoint> | null>(null);

  public isComputing = signal<boolean>(false);

  public paginationArray = computed<number[]>(() => {
    return Array.from(Array(this.geoPointPage()?.page.totalPages!).keys()).map(i => i);
  });

  ngOnInit(): void {
    this.loadData();
  }

  public onCompute() {
    this.distance.set(0);
    this.isComputing.set(true);
    this.positionService.computeDistance(this.pointIdA()!, this.pointIdB()!)
      .subscribe({
        next: distance => {
          this.distance.set(distance);
          this.isComputing.set(false);
        },
        error: () => this.isComputing.set(false)
      });
  }

  onDeleteGeoPoint(point: GeoPoint) {
    this.dialogService.openConfirmDialog("Voulez-vous supprimer N° : ")
      .afterClosed().subscribe(status => {
      if (status) {
        this.positionService.deleteGeoPoint(point.id!)
          .subscribe({
            next: () => {
              this.notifySuccess("La position a été supprimée avec succès.");
              // this.geoPointPage.set(this.positionService.getGeoPoints(this.pageRequest()));
            }
          });
      }
    });
  }

  public goToPage(counter: number) {
    this.pageRequest.set({
      ...this.pageRequest(),
      page: counter
    });
    this.loadData();
  }

  public nextPage() {
    this.goToPage(this.pageRequest().page + 1);
  }

  public previousPage() {
    this.goToPage(this.pageRequest().page - 1);
  }

  private loadData() {
    this.positionService.getGeoPoints(this.pageRequest())
      .subscribe(data => this.geoPointPage.set(data));
  }

  private notifySuccess(message: string) {
    this.matSnackBar.open(message, "Ok", {
      panelClass: "snack-bar-success",
      duration: 5000
    });
  }
}
