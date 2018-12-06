import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from '../core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Holding } from '../shared/model/holding.model';
import { IHolding } from '../shared/model/holding.model';
import { HoldingService } from '../entities/holding/holding.service';
import { HoldingProfilComponent } from './holdingProfil.component';
import { HoldingProfilDetailComponent } from './holdingProfil-detail.component';
import { HoldingProfilUpdateComponent } from './holdingProfil-update.component';
import { HoldingProfilDeletePopupComponent } from './holdingProfil-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class HoldingProfilResolve implements Resolve<IHolding> {
    constructor(private service: HoldingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((holding: HttpResponse<Holding>) => holding.body));
        }
        return of(new Holding());
    }
}

export const holdingProfilRoute: Routes = [
    {
        path: 'holding',
        component: HoldingProfilComponent,
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
        component: HoldingProfilDetailComponent,
        resolve: {
            holding: HoldingProfilResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'holding/new',
        component: HoldingProfilUpdateComponent,
        resolve: {
            holding: HoldingProfilResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'holding/:id/edit',
        component: HoldingProfilUpdateComponent,
        resolve: {
            holding: HoldingProfilResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const holdingProfilPopupRoute: Routes = [
    {
        path: 'holding/:id/delete',
        component: HoldingProfilDeletePopupComponent,
        resolve: {
            holding: HoldingProfilResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.holding.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
