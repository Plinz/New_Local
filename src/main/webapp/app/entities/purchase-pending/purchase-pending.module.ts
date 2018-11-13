import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from 'app/shared';
import { NewLocalAdminModule } from 'app/admin/admin.module';
import {
    PurchasePendingComponent,
    PurchasePendingDetailComponent,
    PurchasePendingUpdateComponent,
    PurchasePendingDeletePopupComponent,
    PurchasePendingDeleteDialogComponent,
    purchasePendingRoute,
    purchasePendingPopupRoute
} from './';

const ENTITY_STATES = [...purchasePendingRoute, ...purchasePendingPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PurchasePendingComponent,
        PurchasePendingDetailComponent,
        PurchasePendingUpdateComponent,
        PurchasePendingDeleteDialogComponent,
        PurchasePendingDeletePopupComponent
    ],
    entryComponents: [
        PurchasePendingComponent,
        PurchasePendingUpdateComponent,
        PurchasePendingDeleteDialogComponent,
        PurchasePendingDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalPurchasePendingModule {}
