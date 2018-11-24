import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from '../shared';
import { NewLocalAdminModule } from '../admin/admin.module';
import { ShoppingComponent, ShoppingDeletePopupComponent, ShoppingDeleteDialogComponent, ShoppingRoute, ShoppingPopupRoute } from './';

const ENTITY_STATES = [...ShoppingRoute, ...ShoppingPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ShoppingComponent, ShoppingDeleteDialogComponent, ShoppingDeletePopupComponent],
    entryComponents: [ShoppingComponent, ShoppingDeleteDialogComponent, ShoppingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalShoppingModule {}
