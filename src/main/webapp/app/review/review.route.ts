import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from '../core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Purchase } from '../shared/model/purchase.model';
import { PurchaseService } from '../entities/purchase/purchase.service';
import { ReviewComponent } from './review.component';

@Injectable({ providedIn: 'root' })
export class ReviewResolve {
    constructor(private service: PurchaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((purchase: HttpResponse<Purchase>) => purchase.body));
        }
        return of(new Purchase());
    }
}

export const ReviewRoute: Routes = [
    {
        path: 'review',
        component: ReviewComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'newLocalApp.purchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
