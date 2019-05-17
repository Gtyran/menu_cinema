import { Component, Inject } from '@angular/core';
import { MatDialogRef, MatSnackBar, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-share-dialog',
  templateUrl: './share-dialog.component.html',
  styleUrls: ['./share-dialog.component.scss']
})
export class ShareDialogComponent   {

  constructor(public dialogRef: MatDialogRef<ShareDialogComponent>,@Inject(MAT_DIALOG_DATA) public data: any,
    public snackBar: MatSnackBar ) { 
  }

  //fermeture du dialog
  public onNoClick(): void {
    this.dialogRef.close();
  }

  //copie de la valeur du lien pris sur le input + fermeture
  public copyLink() {
    const inputElement = document.getElementById('inputId');
    (inputElement as any).select();
    document.execCommand('copy');
    inputElement.blur();
    this.snackBar.open("Le lien est bien été copié", 'OK',{
       duration: 2000 ,
       panelClass: ['success-snackbar']
    });
    this.dialogRef.close();
  }

}
