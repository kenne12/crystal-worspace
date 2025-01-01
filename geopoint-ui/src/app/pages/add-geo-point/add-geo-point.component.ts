import {Component, inject, signal} from '@angular/core';
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {GeoPointService} from '../../services/geo-point.service';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatHint} from '@angular/material/form-field';

@Component({
  selector: 'app-add-geo-point',
  imports: [
    ReactiveFormsModule,
    MatHint
  ],
  templateUrl: './add-geo-point.component.html',
  styleUrl: './add-geo-point.component.scss'
})
export class AddGeoPointComponent {

  private geoPointService = inject(GeoPointService);
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);
  private matSnackBar = inject(MatSnackBar);

  public isLoading = signal<boolean>(false);

  public positionForm = this.formBuilder.group({
    title: new FormControl<string>('', Validators.required),
    longitude: new FormControl<number>(0, Validators.required),
    latitude: new FormControl<number>(0, Validators.required)
  });

  public onSubmit() {
    this.isLoading.set(true);
    this.geoPointService.createGeoPoint({
      title: this.positionForm.value.title!,
      longitude: this.positionForm.value.longitude!,
      latitude: this.positionForm.value.latitude!
    }).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.notifySuccess("La position a été créée avec succès.");
        this.router.navigateByUrl('/geo-points');
      }, error: () => {
        this.isLoading.set(false);
      }
    });
  }

  private notifySuccess(message: string) {
    this.matSnackBar.open(message, "Ok", {
      panelClass: "snack-bar-success",
      duration: 5000
    });
  }
}
