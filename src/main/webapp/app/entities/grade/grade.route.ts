import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Grade } from 'app/shared/model/grade.model';
import { GradeService } from './grade.service';
import { GradeComponent } from './grade.component';
import { GradeDetailComponent } from './grade-detail.component';
import { GradeUpdateComponent } from './grade-update.component';
import { GradeDeletePopupComponent } from './grade-delete-dialog.component';
import { IGrade } from 'app/shared/model/grade.model';

@Injectable({ providedIn: 'root' })
export class GradeResolve implements Resolve<IGrade> {
    constructor(private service: GradeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((grade: HttpResponse<Grade>) => grade.body));
        }
        return of(new Grade());
    }
}

export const gradeRoute: Routes = [
    {
        path: 'grade',
        component: GradeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'newLocalApp.grade.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'grade/:id/view',
        component: GradeDetailComponent,
        resolve: {
            grade: GradeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.grade.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'grade/new',
        component: GradeUpdateComponent,
        resolve: {
            grade: GradeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.grade.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'grade/:id/edit',
        component: GradeUpdateComponent,
        resolve: {
            grade: GradeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.grade.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gradePopupRoute: Routes = [
    {
        path: 'grade/:id/delete',
        component: GradeDeletePopupComponent,
        resolve: {
            grade: GradeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.grade.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
