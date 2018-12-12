import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Stock } from 'app/shared/model/stock.model';
import { StockService } from '../entities/stock/stock.service';
import { MainSearchComponent } from './mainSearch.component';
import { MainSearchDetailComponent } from './mainSearch-detail.component';
import { IStock } from 'app/shared/model/stock.model';

@Injectable({ providedIn: 'root' })
export class StockResolve implements Resolve<IStock> {
    constructor(private service: StockService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((stock: HttpResponse<Stock>) => stock.body));
        }
        return of(new Stock());
    }
}

export const mainSearchRoute: Routes = [
    {
        path: 'mainSearch',
        component: MainSearchComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            defaultSort: 'id,asc',
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mainSearch/:id/view',
        component: MainSearchDetailComponent,
        resolve: {
            stock: StockResolve
        },
        data: {
            pageTitle: 'newLocalApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
