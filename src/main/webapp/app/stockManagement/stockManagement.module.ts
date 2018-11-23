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
import { PopupComponent } from './popup/popup.component';

const ENTITY_STATES = [...stockManagementRoute, ...stockManagementPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StockManagementComponent,
        StockManagementDetailComponent,
        StockManagementUpdateComponent,
        StockManagementDeleteDialogComponent,
        StockManagementDeletePopupComponent,
        PopupComponent
    ],
    entryComponents: [
        StockManagementComponent,
        StockManagementUpdateComponent,
        StockManagementDeleteDialogComponent,
        StockManagementDeletePopupComponent,
        PopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalStockManagementModule {}
