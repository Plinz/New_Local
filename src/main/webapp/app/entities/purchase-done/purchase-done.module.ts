import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from 'app/shared';
import { NewLocalAdminModule } from 'app/admin/admin.module';
import {
    PurchaseDoneComponent,
    PurchaseDoneDetailComponent,
    PurchaseDoneUpdateComponent,
    PurchaseDoneDeletePopupComponent,
    PurchaseDoneDeleteDialogComponent,
    purchaseDoneRoute,
    purchaseDonePopupRoute
} from './';

const ENTITY_STATES = [...purchaseDoneRoute, ...purchaseDonePopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PurchaseDoneComponent,
        PurchaseDoneDetailComponent,
        PurchaseDoneUpdateComponent,
        PurchaseDoneDeleteDialogComponent,
        PurchaseDoneDeletePopupComponent
    ],
    entryComponents: [
        PurchaseDoneComponent,
        PurchaseDoneUpdateComponent,
        PurchaseDoneDeleteDialogComponent,
        PurchaseDoneDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalPurchaseDoneModule {}
