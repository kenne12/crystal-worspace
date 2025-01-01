import {inject, Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConfirmDialogComponent} from '../components/confirm-dialog/confirm-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  private dialog: MatDialog = inject(MatDialog);

  openConfirmDialog(msg: string): MatDialogRef<any> {
    return this.dialog.open(ConfirmDialogComponent, {
      width: "450px",
      panelClass: "confirm-dialogs-dialogs-container",
      disableClose: true,
      data: {
        message: msg
      }
    });
  }
}
