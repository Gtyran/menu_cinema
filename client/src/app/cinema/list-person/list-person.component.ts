import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatSnackBar} from '@angular/material';
import {PersonResponse} from '../../interface/tmdb-data/Person';

@Component({
    selector: 'list-person',
    templateUrl: './list-person.component.html',
    styleUrls: ['./list-person.component.scss']
})
export class ListPersonComponent implements OnInit {
    @Input() personList: PersonResponse;
    @Input() affResPerson: string;

    constructor(private route: Router, private snackBar: MatSnackBar, public dialog: MatDialog,) {
    }

    ngOnInit() {
    }

    //redirection vers la page info du film
    public ficheFilm(id: number) {
        if (id !== null) {
            this.route.navigate(['film/' + id]);
        }
    }


}
