import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from '../shared';
import { NewLocalAdminModule } from '../admin/admin.module';
import {
    StockManagementComponent,
    StockManagementDetailComponent,
    StockManagementUpdateComponent,
    StockManagementDeletePopupComponent,
    StockManagementDeleteDialogComponent,
    stockManagementRoute,
    stockManagementPopupRoute
} from './';

const ENTITY_STATES = [...stockManagementRoute, ...stockManagementPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StockManagementComponent,
        StockManagementDetailComponent,
        StockManagementUpdateComponent,
        StockManagementDeleteDialogComponent,
        StockManagementDeletePopupComponent
    ],
    entryComponents: [
        StockManagementComponent,
        StockManagementUpdateComponent,
        StockManagementDeleteDialogComponent,
        StockManagementDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalStockManagementModule {}
