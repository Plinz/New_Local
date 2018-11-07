import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGrade } from 'app/shared/model/grade.model';

@Component({
    selector: 'jhi-grade-detail',
    templateUrl: './grade-detail.component.html'
})
export class GradeDetailComponent implements OnInit {
    grade: IGrade;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ grade }) => {
            this.grade = grade;
        });
    }

    previousState() {
        window.history.back();
    }
}
