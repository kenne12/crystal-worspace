import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {catchError, Observable, of, throwError} from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const matSnackBar = inject(MatSnackBar);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      processErrorMessage(error)
        .subscribe(message => matSnackBar.open(message, undefined, {panelClass: "snack-bar-error", duration: 5000}));
      return throwError(() => error.error);
    })
  );
};

function processErrorMessage(error: HttpErrorResponse): Observable<string> {
  return of(error.error.message || error.error.errors.join(", "));
}
