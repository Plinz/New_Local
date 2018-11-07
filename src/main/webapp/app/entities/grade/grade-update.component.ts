import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGrade } from 'app/shared/model/grade.model';
import { GradeService } from './grade.service';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person';
import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from 'app/entities/product-type';

@Component({
    selector: 'jhi-grade-update',
    templateUrl: './grade-update.component.html'
})
export class GradeUpdateComponent implements OnInit {
    grade: IGrade;
    isSaving: boolean;

    people: IPerson[];

    producttypes: IProductType[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private gradeService: GradeService,
        private personService: PersonService,
        private productTypeService: ProductTypeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ grade }) => {
            this.grade = grade;
        });
        this.personService.query().subscribe(
            (res: HttpResponse<IPerson[]>) => {
                this.people = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.productTypeService.query().subscribe(
            (res: HttpResponse<IProductType[]>) => {
                this.producttypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.grade.id !== undefined) {
            this.subscribeToSaveResponse(this.gradeService.update(this.grade));
        } else {
            this.subscribeToSaveResponse(this.gradeService.create(this.grade));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGrade>>) {
        result.subscribe((res: HttpResponse<IGrade>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPersonById(index: number, item: IPerson) {
        return item.id;
    }

    trackProductTypeById(index: number, item: IProductType) {
        return item.id;
    }
}
