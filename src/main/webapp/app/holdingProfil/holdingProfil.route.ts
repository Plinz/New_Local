import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Holding } from 'app/shared/model/holding.model';
import { HoldingService } from './holding.service';
import { HoldingComponent } from './holding.component';
import { HoldingDetailComponent } from './holding-detail.component';
import { HoldingUpdateComponent } from './holding-update.component';
import { HoldingDeletePopupComponent } from './holding-delete-dialog.component';
import { IHolding } from 'app/shared/model/holding.model';

@Injectable({ providedIn: 'root' })
export class HoldingResolve implements Resolve<IHolding> {
    constructor(private service: HoldingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((holding: HttpResponse<Holding>) => holding.body));
        }
        return of(new Holding());
    }
}

export const holdingRoute: Routes = [
    {
        path: 'holding',
        component: HoldingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'holding/:id/view',
        component: HoldingDetailComponent,
        resolve: {
            holding: HoldingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'holding/new',
        component: HoldingUpdateComponent,
        resolve: {
            holding: HoldingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'holding/:id/edit',
        component: HoldingUpdateComponent,
        resolve: {
            holding: HoldingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const holdingPopupRoute: Routes = [
    {
        path: 'holding/:id/delete',
        component: HoldingDeletePopupComponent,
        resolve: {
            holding: HoldingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
