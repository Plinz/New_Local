import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-review',
    templateUrl: './review.component.html',
    styles: []
})
export class ReviewComponent implements OnInit {
    count: number;
    constructor() {
        this.count = 1;
    }

    ngOnInit() {}

    onClickMe(i: number) {
        this.count = i;
    }
}
