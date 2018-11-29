import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchase } from '../shared/model/purchase.model';
import { PurchaseService } from '../entities/purchase/purchase.service';

@Component({
    selector: 'jhi-purchase-delete-dialog',
    templateUrl: './shopping-delete-dialog.component.html'
})
export class ShoppingDeleteDialogComponent {
    purchase: IPurchase;

    constructor(private purchaseService: PurchaseService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'purchaseListModification',
                content: 'Deleted an purchase'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-delete-popup',
    template: ''
})
export class ShoppingDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchase }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ShoppingDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.purchase = purchase;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
